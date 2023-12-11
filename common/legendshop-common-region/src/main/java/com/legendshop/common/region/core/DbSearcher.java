/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.region.core;

import com.legendshop.common.region.service.impl.RandomAccessFileDBReader;
import com.legendshop.common.region.util.RegionUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

/**
 * @author legendshop
 */
public class DbSearcher {
	public static final int BTREE_ALGORITHM = 1;
	public static final int BINARY_ALGORITHM = 2;
	public static final int MEMORY_ALGORITYM = 3;

	/**
	 * db config
	 */
	private final DbConfig dbConfig;

	/**
	 * db file access handler
	 */
	private final DBReader reader;

	/**
	 * header blocks buffer
	 */
	private long[] HeaderSip = null;
	private int[] HeaderPtr = null;
	private int headerLength;

	/**
	 * super blocks info
	 */
	private long firstIndexPtr = 0;
	private long lastIndexPtr = 0;
	private int totalIndexBlocks = 0;

	/**
	 * for memory mode
	 * the original db binary string
	 */
	private byte[] dbBinStr = null;

	/**
	 * construct class
	 *
	 * @param dbConfig dbConfig
	 * @param dbFile   dbFile
	 * @throws FileNotFoundException
	 */
	public DbSearcher(DbConfig dbConfig, String dbFile) throws FileNotFoundException {
		this(dbConfig, new RandomAccessFileDBReader(new RandomAccessFile(dbFile, "r")));
	}

	public DbSearcher(DbConfig dbConfig, DBReader reader) {
		this.dbConfig = dbConfig;
		this.reader = reader;
	}

	/**
	 * get the region with a int ip address with memory binary search algorithm
	 *
	 * @param ip
	 * @throws IOException
	 */
	public DataBlock memorySearch(long ip) throws IOException {
		int blen = IndexBlock.getIndexBlockLength();
		if (dbBinStr == null) {
			dbBinStr = reader.full();

			//initialize the global vars
			firstIndexPtr = RegionUtil.getIntLong(dbBinStr, 0);
			lastIndexPtr = RegionUtil.getIntLong(dbBinStr, 4);
			totalIndexBlocks = (int) ((lastIndexPtr - firstIndexPtr) / blen) + 1;
		}

		//search the index blocks to define the data
		int l = 0, h = totalIndexBlocks;
		long sip, eip, dataptr = 0;
		while (l <= h) {
			int m = (l + h) >> 1;
			int p = (int) (firstIndexPtr + m * blen);

			sip = RegionUtil.getIntLong(dbBinStr, p);
			if (ip < sip) {
				h = m - 1;
			} else {
				eip = RegionUtil.getIntLong(dbBinStr, p + 4);
				if (ip > eip) {
					l = m + 1;
				} else {
					dataptr = RegionUtil.getIntLong(dbBinStr, p + 8);
					break;
				}
			}
		}

		//not matched
		if (dataptr == 0) {
			return null;
		}

		//get the data
		int dataLen = (int) ((dataptr >> 24) & 0xFF);
		int dataPtr = (int) ((dataptr & 0x00FFFFFF));
		int city_id = (int) RegionUtil.getIntLong(dbBinStr, dataPtr);
		String region = new String(dbBinStr, dataPtr + 4, dataLen - 4, StandardCharsets.UTF_8);

		return new DataBlock(city_id, region, dataPtr);
	}

	/**
	 * get the region throught the ip address with memory binary search algorithm
	 *
	 * @param ip
	 * @return DataBlock
	 * @throws IOException
	 */
	public DataBlock memorySearch(String ip) throws IOException {
		return memorySearch(RegionUtil.ip2long(ip));
	}

	/**
	 * get by index ptr
	 *
	 * @param ptr ptr
	 * @throws IOException
	 */
	public DataBlock getByIndexPtr(long ptr) throws IOException {
		byte[] buffer = new byte[12];
		reader.readFully(ptr, buffer, 0, buffer.length);
		long extra = RegionUtil.getIntLong(buffer, 8);

		int dataLen = (int) ((extra >> 24) & 0xFF);
		int dataPtr = (int) ((extra & 0x00FFFFFF));

		byte[] data = new byte[dataLen];
		reader.readFully(dataPtr, data, 0, data.length);

		int city_id = (int) RegionUtil.getIntLong(data, 0);
		String region = new String(data, 4, data.length - 4, StandardCharsets.UTF_8);

		return new DataBlock(city_id, region, dataPtr);
	}

	/**
	 * get the region with a int ip address with b-tree algorithm
	 *
	 * @param ip
	 * @throws IOException
	 */
	public DataBlock btreeSearch(long ip) throws IOException {
		if (HeaderSip == null) {
			byte[] b = new byte[4096];
			//pass the super block
			reader.readFully(8L, b, 0, b.length);

			//fill the header
			//b.lenght / 8
			int len = b.length >> 3, idx = 0;
			long[] HeaderSip = new long[len];
			int[] HeaderPtr = new int[len];
			long startIp, dataPtr;
			for (int i = 0; i < b.length; i += 8) {
				startIp = RegionUtil.getIntLong(b, i);
				dataPtr = RegionUtil.getIntLong(b, i + 4);
				if (dataPtr == 0) {
					break;
				}

				HeaderSip[idx] = startIp;
				HeaderPtr[idx] = (int) dataPtr;
				idx++;
			}

			headerLength = idx;
			this.HeaderPtr = HeaderPtr;
			this.HeaderSip = HeaderSip;
		}

		//1. define the index block with the binary search
		if (ip == HeaderSip[0]) {
			return getByIndexPtr(HeaderPtr[0]);
		} else if (ip == HeaderSip[headerLength - 1]) {
			return getByIndexPtr(HeaderPtr[headerLength - 1]);
		}

		int l = 0, h = headerLength, sptr = 0, eptr = 0;
		while (l <= h) {
			int m = (l + h) >> 1;

			//perfetc matched, just return it
			if (ip == HeaderSip[m]) {
				if (m > 0) {
					sptr = HeaderPtr[m - 1];
					eptr = HeaderPtr[m];
				} else {
					sptr = HeaderPtr[m];
					eptr = HeaderPtr[m + 1];
				}

				break;
			}

			//less then the middle value
			if (ip < HeaderSip[m]) {
				if (m == 0) {
					sptr = HeaderPtr[m];
					eptr = HeaderPtr[m + 1];
					break;
				} else if (ip > HeaderSip[m - 1]) {
					sptr = HeaderPtr[m - 1];
					eptr = HeaderPtr[m];
					break;
				}
				h = m - 1;
			} else {
				if (m == headerLength - 1) {
					sptr = HeaderPtr[m - 1];
					eptr = HeaderPtr[m];
					break;
				} else if (ip <= HeaderSip[m + 1]) {
					sptr = HeaderPtr[m];
					eptr = HeaderPtr[m + 1];
					break;
				}
				l = m + 1;
			}
		}

		//match nothing just stop it
		if (sptr == 0) {
			return null;
		}

		//2. search the index blocks to define the data
		int blockLen = eptr - sptr, blen = IndexBlock.getIndexBlockLength();
		//include the right border block
		byte[] iBuffer = new byte[blockLen + blen];
		reader.readFully(sptr, iBuffer, 0, iBuffer.length);

		l = 0;
		h = blockLen / blen;
		long sip, eip, dataptr = 0;
		while (l <= h) {
			int m = (l + h) >> 1;
			int p = m * blen;
			sip = RegionUtil.getIntLong(iBuffer, p);
			if (ip < sip) {
				h = m - 1;
			} else {
				eip = RegionUtil.getIntLong(iBuffer, p + 4);
				if (ip > eip) {
					l = m + 1;
				} else {
					dataptr = RegionUtil.getIntLong(iBuffer, p + 8);
					break;
				}
			}
		}

		//not matched
		if (dataptr == 0) {
			return null;
		}

		//3. get the data
		int dataLen = (int) ((dataptr >> 24) & 0xFF);
		int dataPtr = (int) ((dataptr & 0x00FFFFFF));

		byte[] data = new byte[dataLen];
		reader.readFully(dataPtr, data, 0, data.length);

		int city_id = (int) RegionUtil.getIntLong(data, 0);
		String region = new String(data, 4, data.length - 4, StandardCharsets.UTF_8);

		return new DataBlock(city_id, region, dataPtr);
	}

	/**
	 * get the region throught the ip address with b-tree search algorithm
	 *
	 * @param ip
	 * @return DataBlock
	 * @throws IOException
	 */
	public DataBlock btreeSearch(String ip) throws IOException {
		return btreeSearch(RegionUtil.ip2long(ip));
	}

	/**
	 * get the region with a int ip address with binary search algorithm
	 *
	 * @param ip
	 * @throws IOException
	 */
	public DataBlock binarySearch(long ip) throws IOException {
		int blen = IndexBlock.getIndexBlockLength();
		if (totalIndexBlocks == 0) {
			byte[] superBytes = new byte[8];
			reader.readFully(0L, superBytes, 0, superBytes.length);
			//initialize the global vars
			firstIndexPtr = RegionUtil.getIntLong(superBytes, 0);
			lastIndexPtr = RegionUtil.getIntLong(superBytes, 4);
			totalIndexBlocks = (int) ((lastIndexPtr - firstIndexPtr) / blen) + 1;
		}

		//search the index blocks to define the data
		int l = 0, h = totalIndexBlocks;
		byte[] buffer = new byte[blen];
		long sip, eip, dataptr = 0;
		while (l <= h) {
			int m = (l + h) >> 1;
			//reader.seek(firstIndexPtr + m * blen);    //set the file pointer
			reader.readFully(firstIndexPtr + m * blen, buffer, 0, buffer.length);
			sip = RegionUtil.getIntLong(buffer, 0);
			if (ip < sip) {
				h = m - 1;
			} else {
				eip = RegionUtil.getIntLong(buffer, 4);
				if (ip > eip) {
					l = m + 1;
				} else {
					dataptr = RegionUtil.getIntLong(buffer, 8);
					break;
				}
			}
		}

		//not matched
		if (dataptr == 0) {
			return null;
		}

		//get the data
		int dataLen = (int) ((dataptr >> 24) & 0xFF);
		int dataPtr = (int) ((dataptr & 0x00FFFFFF));

		byte[] data = new byte[dataLen];
		reader.readFully(dataPtr, data, 0, data.length);

		int city_id = (int) RegionUtil.getIntLong(data, 0);
		String region = new String(data, 4, data.length - 4, StandardCharsets.UTF_8);

		return new DataBlock(city_id, region, dataPtr);
	}

	/**
	 * get the region throught the ip address with binary search algorithm
	 *
	 * @param ip
	 * @return DataBlock
	 * @throws IOException
	 */
	public DataBlock binarySearch(String ip) throws IOException {
		return binarySearch(RegionUtil.ip2long(ip));
	}

	/**
	 * get the db config
	 *
	 * @return DbConfig
	 */
	public DbConfig getDbConfig() {
		return dbConfig;
	}

	/**
	 * close the db
	 *
	 * @throws IOException
	 */
	public void close() throws IOException {
		//let gc do its work
		HeaderSip = null;
		HeaderPtr = null;
		dbBinStr = null;
		reader.close();
	}

}
