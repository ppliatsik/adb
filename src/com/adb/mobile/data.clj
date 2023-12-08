(ns com.adb.mobile.data
  (:require [hugsql.core :as hugs]
            [clojure.java.io :as clj.io]
            [clojure.string :as clj.str]
            [clojure.set :as clj.set]
            [java-time :as jt])
  (:import [java.io ByteArrayOutputStream]))

(hugs/def-db-fns "com/adb/mobile/sql/mobile.sql")
(hugs/def-sqlvec-fns "com/adb/mobile/sql/mobile.sql")

(defn- read-file-data
  [input-file]
  (with-open [stream (clj.io/input-stream input-file)]
    (let [baos (ByteArrayOutputStream.)
          _    (.transferTo stream baos)]
      (.toByteArray baos))))

(defn save-mobile-photos
  [db data]
  (doseq [{:keys [file-paths device-id]} data]
    (->> file-paths
         (map (fn [file-path]
                (let [file-name (-> file-path
                                    (clj.str/split #"/")
                                    last)
                      params    {:device-id device-id
                                 :file-data (read-file-data file-path)
                                 :file-path file-path
                                 :file-name file-name}]
                  (save-mobile-photo db params))))
         doall)))

(defn- get-params
  [device-id record]
  (-> record
      (update :date (fn [date]
                      (when-not (clj.str/blank? date)
                        (let [instant (jt/instant (Long/parseLong date))]
                          (jt/zoned-date-time instant (jt/zone-id))))))
      (update :seen #(= "1" %))
      (clj.set/rename-keys {:date :created-at :data-1 :number})
      (assoc :device-id device-id)))

(defn save-mobile-calls
  [db data]
  (doseq [{:keys [result device-id]} data]
    (->> result
         (map (fn [record]
                (let [params (get-params device-id record)]
                  (save-mobile-call db params))))
         doall)))

(defn save-mobile-contacts
  [db data]
  (doseq [{:keys [result device-id]} data]
    (->> result
         (map (fn [record]
                (let [params (get-params device-id record)]
                  (save-mobile-contact db params))))
         doall)))

(defn save-mobile-sms
  [db data]
  (doseq [{:keys [result device-id]} data]
    (->> result
         (map (fn [record]
                (let [params (get-params device-id record)]
                  (save-mobile-sms* db params))))
         doall)))
