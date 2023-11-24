(ns com.adb.mobile.api
  (:require [clojure.java.io :as clj.io]
            [clojure.string :as clj.str]
            [com.adb.system.android :as system.android]
            [com.adb.mobile.data :as mobile.data]))

(defn save-all
  [{:keys [db download-path android-base-path adb-devices] :as request}]
  (let [data (system.android/download-device-data request)]
    (mobile.data/save-all-mobile-data db data)
    (if (> (count adb-devices) 0)
      {:status 200
       :result :success}
      {:status 400
       :result :no-device-found})))

(defn get-all
  [{:keys [db] :as request}]
  (let [data (mobile.data/get-mobile-data db)]
    {:status 200
     :body   data}))

(defn get-record
  [{:keys [db parameters] :as request}]
  (let [record (mobile.data/get-mobile-data-record db (:path parameters))]
    (if record
      {:status  200
       :body    record}
      {:status 404
       :result :not-found})))

(defn delete-record
  [{:keys [db parameters] :as request}]
  (mobile.data/delete-mobile-data-record db (:path parameters))
  {:status 200
   :result :success})

(defn show-record-image
  [{:keys [db parameters] :as request}]
  (if-let [record (mobile.data/get-mobile-data-record-image db (:path parameters))]
    (let [file-type    (last (clj.str/split (:file-name record) #"."))
          content-type (cond (or (= "jpg" file-type)
                                 (= "jpeg" file-type))
                             "image/jpeg"

                             (= "png" file-type)
                             "image/png"

                             :else
                             "application/octet-stream")]
      {:status  200
       :headers {"Content-Type" content-type}
       :body    (clj.io/input-stream (:file-data record))})
    {:status 404
     :result :not-found}))
