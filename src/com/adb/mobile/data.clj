(ns com.adb.mobile.data
  (:require [hugsql.core :as hugs]
            [clojure.java.io :as clj.io]
            [clojure.string :as clj.str])
  (:import [java.io ByteArrayOutputStream]))

(hugs/def-db-fns "com/adb/mobile/sql/mobile.sql")
(hugs/def-sqlvec-fns "com/adb/mobile/sql/mobile.sql")

(defn- read-file-data
  [input-file]
  (with-open [stream (clj.io/input-stream input-file)]
    (let [baos (ByteArrayOutputStream.)
          _    (.transferTo stream baos)]
      (.toByteArray baos))))

(defn save-all-mobile-data
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
                  (save-mobile-data db params))))
         doall)))
