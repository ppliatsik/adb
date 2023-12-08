(ns com.adb.mobile.routes
  (:require [com.adb.mobile :as mobile]
            [com.adb.mobile.api :as mobile.api]))

(def routes
  [["/mobile"
    ["/photos"
     {:name    ::mobile/photos
      :swagger {:tags ["mobile"]}
      :get     {:summary "Get mobile photos"
                :handler mobile.api/get-photos}
      :post    {:summary "Save photos from mobile to db"
                :handler mobile.api/save-photos}}]
    ["/photos/:device-id/:file-name/image.png"
     {:name    ::mobile/photo-record-image
      :swagger {:tags ["mobile"]}
      :get     {:summary    "Show mobile photo data image"
                :parameters {:path {:device-id ::mobile/device-id
                                    :file-name ::mobile/file-name}}
                :handler    mobile.api/show-photo-record-image}}]
    ["/calls"
     {:name    ::mobile/calls
      :swagger {:tags ["mobile"]}
      :get     {:summary "Get mobile calls"
                :handler mobile.api/get-calls}
      :post    {:summary "Save calls from mobile to db"
                :handler mobile.api/save-calls}}]
    ["/contacts"
     {:name    ::mobile/contacts
      :swagger {:tags ["mobile"]}
      :get     {:summary "Get mobile contacts"
                :handler mobile.api/get-contacts}
      :post    {:summary "Save contacts from mobile to db"
                :handler mobile.api/save-contacts}}]
    ["/sms"
     {:name    ::mobile/sms
      :swagger {:tags ["mobile"]}
      :get     {:summary "Get mobile sms"
                :handler mobile.api/get-sms}
      :post    {:summary "Save sms from mobile to db"
                :handler mobile.api/save-sms}}]]])
