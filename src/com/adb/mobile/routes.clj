(ns com.adb.mobile.routes
  (:require [com.adb.mobile :as mobile]
            [com.adb.mobile.api :as mobile.api]))

(def routes
  [["/mobile"
    [""
     {:name    ::mobile/mobile-data
      :swagger {:tags ["mobile"]}
      :get     {:summary "Get mobile data"
                :handler mobile.api/get-all}
      :post    {:summary "Save data from mobile to db"
                :handler mobile.api/save-all}}]
    ["/:device-id/:file-name"
     {:name    ::mobile/mobile-data-record
      :swagger {:tags ["mobile"]}
      :get     {:summary    "Get mobile data by id"
                :parameters {:path {:device-id ::mobile/device-id
                                    :file-name ::mobile/file-name}}
                :handler    mobile.api/get-record}
      :delete  {:summary    "Delete mobile data by id"
                :parameters {:path {:device-id ::mobile/device-id
                                    :file-name ::mobile/file-name}}
                :handler    mobile.api/delete-record}}]
    ["/:device-id/:file-name/image.png"
     {:name    ::mobile/mobile-data-record-image
      :swagger {:tags ["mobile"]}
      :get     {:summary    "Show mobile data image"
                :parameters {:path {:device-id ::mobile/device-id
                                    :file-name ::mobile/file-name}}
                :handler    mobile.api/show-record-image}}]]])
