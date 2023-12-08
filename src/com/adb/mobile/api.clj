(ns com.adb.mobile.api
  (:require [clojure.java.io :as clj.io]
            [clojure.string :as clj.str]
            [com.adb.system.android :as system.android]
            [com.adb.mobile.data :as mobile.data]))

(defn save-photos
  [{:keys [db download-path android-base-path adb-devices] :as request}]
  (let [data (system.android/download-devices-photos request)]
    (mobile.data/save-mobile-photos db data)
    (if (seq adb-devices)
      {:status 200
       :result :success}
      {:status 400
       :result :no-device-found})))

(defn get-photos
  [{:keys [db] :as request}]
  (let [data (mobile.data/get-mobile-photos db)]
    {:status 200
     :body   data}))

(defn show-photo-record-image
  [{:keys [db parameters] :as request}]
  (if-let [record (mobile.data/get-mobile-photo-record-image db (:path parameters))]
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

(defn save-calls
  [{:keys [db adb-shell-command adb-devices] :as request}]
  (let [data (system.android/download-devices-calls request)]
    (mobile.data/save-mobile-calls db data)
    (if (seq adb-devices)
      {:status 200
       :result :success}
      {:status 400
       :result :no-device-found})))

(defn get-calls
  [{:keys [db] :as request}]
  (let [data (mobile.data/get-mobile-calls db)]
    {:status 200
     :body   data}))

(defn save-contacts
  [{:keys [db adb-shell-command adb-devices] :as request}]
  (let [data (system.android/download-devices-contacts request)]
    (mobile.data/save-mobile-contacts db data)
    (if (seq adb-devices)
      {:status 200
       :result :success}
      {:status 400
       :result :no-device-found})))

(defn get-contacts
  [{:keys [db] :as request}]
  (let [data (mobile.data/get-mobile-contacts db)]
    {:status 200
     :body   data}))

(defn save-sms
  [{:keys [db adb-shell-command adb-devices] :as request}]
  (let [data (system.android/download-devices-sms request)]
    (mobile.data/save-mobile-sms db data)
    (if (seq adb-devices)
      {:status 200
       :result :success}
      {:status 400
       :result :no-device-found})))

(defn get-sms
  [{:keys [db] :as request}]
  (let [data (mobile.data/get-mobile-sms db)]
    {:status 200
     :body   data}))
