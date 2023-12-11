#!/bin/sh

legendshop_log() {
        local type="$1"; shift
        printf '%s [%s] [Entrypoint]: %s\n' "$(date --rfc-3339=seconds)" "$type" "$*"
}

#错误输出
legendshop_error() {
        legendshop_log ERROR "$@" >&2
        exit 1
}

#初始化参数
licenseParam=""
nacosParam=""
legednshop_init_param() {
   licenseParam="http://192.168.0.11:15151"
   for license in $(echo $LICENSE_URL);do
     echo "找到 license 参数为：${license}"
     licenseParam="$license"
   done
   echo "license 参数为：${licenseParam}"

   #nacosParam="-DNACOS_HOST=192.168.0.15 -DNACOS_PORT=8848 -DNACOS_NAMESPACE=dev-sr1 -DNACOS_USERNAME=nacos -DNACOS_PASSWORD=nacos"
   for nacos in $(echo $NACOS_SETTING); do
       echo "找到 nacos 参数为：${nacos}"
        nacosParam="${nacosParam} $nacos"
   done
   echo "nacos 参数为：${nacosParam}"
}

#主方法
_main() {
        legednshop_init_param "$@"
}

_main "$@"

java -javaagent:"/legendshop-auth/service-license-fatjar-6.0.0.jar=-url ${licenseParam}" ${nacosParam} -jar /legendshop-auth/legendshop-auth.jar
