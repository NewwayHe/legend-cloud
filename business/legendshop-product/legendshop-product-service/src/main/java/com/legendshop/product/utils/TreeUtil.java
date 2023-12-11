/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.utils;

import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.dto.MenuTree;
import com.legendshop.user.dto.TreeNode;
import com.legendshop.user.enums.MenuTypeEnum;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 树形工具类
 *
 * @author legendshop
 */
@UtilityClass
public class TreeUtil {

	/**
	 * 两层循环实现建树
	 *
	 * @param treeNodes 传入的树节点列表
	 * @return
	 */
	public <T extends TreeNode> List<T> build(List<T> treeNodes, Long root) {
		List<T> trees = new ArrayList<>();
		for (T treeNode : treeNodes) {
			if (root.equals(treeNode.getParentId())) {
				trees.add(treeNode);
			}
			for (T it : treeNodes) {
				if (it.getParentId().equals(treeNode.getId())) {
					if (treeNode.getChildren() == null) {
						treeNode.setChildren(new ArrayList<>());
					}
					treeNode.add(it);
				}
			}
		}
		return trees;
	}

	/**
	 * 使用递归方法建树
	 *
	 * @param treeNodes
	 * @return
	 */
	public <T extends TreeNode> List<T> buildByRecursive(List<T> treeNodes, Long root) {
		List<T> trees = new ArrayList<T>();
		for (T treeNode : treeNodes) {
			if (root.equals(treeNode.getParentId())) {
				trees.add(findChildren(treeNode, treeNodes));
			}
		}
		return trees;
	}

	/**
	 * 递归查找子节点
	 *
	 * @param treeNodes
	 * @return
	 */
	public <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
		for (T it : treeNodes) {
			if (treeNode.getId().equals(it.getParentId())) {
				if (treeNode.getChildren() == null) {
					treeNode.setChildren(new ArrayList<>());
				}
				treeNode.add(findChildren(it, treeNodes));
			}
		}
		return treeNode;
	}

	/**
	 * 通过sysMenu创建树形节点
	 *
	 * @param menus
	 * @param root
	 * @return
	 */
	public List<MenuTree> buildTree(List<MenuBO> menus, Long root) {
		List<MenuTree> trees = new ArrayList<>();
		MenuTree node;
		for (MenuBO menu : menus) {
			node = new MenuTree();
			node.setId(menu.getId());
			node.setParentId(menu.getParentId());
			node.setName(menu.getName());
			node.setPath(menu.getPath());
			node.setComponent(menu.getComponent());
			node.setPermission(menu.getPermission());
			node.setLabel(menu.getName());
			node.setIcon(menu.getIcon());
			node.setType(menu.getType());
			node.setSort(menu.getSort());
			node.setHasChildren(true);
			node.setHiddenFlag(menu.getHiddenFlag());
			trees.add(node);
		}
		return TreeUtil.build(trees, root);
	}

	public List<MenuTree> filterMenu(Set<MenuBO> all, String type, Long parentId) {
		if (!CollectionUtils.isEmpty(all)) {
			List<MenuTree> menuTreeList = all.stream().filter(menuTypePredicate(type)).map(MenuTree::new)
					.sorted(Comparator.comparingDouble(MenuTree::getSort)).collect(Collectors.toList());
			Long parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;
			return TreeUtil.build(menuTreeList, parent);
		}
		return null;
	}

	private Predicate<MenuBO> menuTypePredicate(String type) {
		return vo -> {
			if (MenuTypeEnum.TOP_MENU.getDescription().equals(type)) {
				return MenuTypeEnum.TOP_MENU.getType().equals(vo.getType());
			}
			// 其他查询 左侧 + 顶部
			return !MenuTypeEnum.BUTTON.getType().equals(vo.getType());
		};
	}

}
