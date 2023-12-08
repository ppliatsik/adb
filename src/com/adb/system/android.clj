(ns com.adb.system.android
  (:require [integrant.core :as ig]
            [clojure.tools.logging :as log]
            [clojure.string :as clj.str]
            [camel-snake-kebab.core :as csk])
  (:import [java.io File]
           [java.nio.charset StandardCharsets]
           [java.util.stream Collectors ReferencePipeline]
           [se.vidstige.jadb JadbConnection JadbDevice RemoteFile]))

(defmethod ig/init-key :adb/devices [_ spec]
  (let [jadb    (JadbConnection.)
        devices (.getDevices jadb)]
    (log/info "Android devices found: " (count devices))
    devices))

(defmethod ig/halt-key! :adb/devices [_ adb-devices]
  )

(defn download-devices-photos
  [{:keys [download-path android-base-path adb-devices]}]
  (->> adb-devices
       (map (fn [adb-device]
              (let [device-id  (.getSerial adb-device)
                    file-paths (->> (.list ^JadbDevice adb-device android-base-path)
                                    (filter #(not (.isDirectory %)))
                                    (reduce (fn [acc remote-file]
                                              (let [remote-path (str android-base-path (.getPath remote-file))
                                                    local-path  (str download-path (.getPath remote-file))]
                                                (.pull adb-device (RemoteFile. remote-path) (File. local-path))
                                                (conj acc local-path)))
                                            []))]
                {:device-id  device-id
                 :file-paths file-paths})))
       doall))

(defn- execute-shell
  [^JadbDevice adb-device command args]
  (as-> (.executeShell adb-device command args) $
        (String. (.readAllBytes $) StandardCharsets/UTF_8)
        (.lines $)
        (.collect ^ReferencePipeline $ (Collectors/toList))))

(defn- shell-result->edn
  [result]
  (->> result
       (map (fn [sms]
              (as-> sms $
                    (clj.str/split $ #", ")
                    (reduce (fn [acc data]
                              (let [kv (clj.str/split data #"=")
                                    k  (if (clj.str/includes? (first kv) " _")
                                         (-> (first kv)
                                             (clj.str/split #"_")
                                             second
                                             keyword)
                                         (-> kv first csk/->kebab-case-keyword))]
                                (assoc acc k (second kv))))
                            {}
                            $))))
       vec))

(defn- get-results
  [adb-shell-command adb-devices args]
  (->> adb-devices
       (map (fn [adb-device]
              (let [result (-> (execute-shell adb-device adb-shell-command args)
                               shell-result->edn)]
                {:device-id  (.getSerial adb-device)
                 :result     result})))
       doall))

(defn download-devices-calls
  [{:keys [adb-shell-command adb-devices]}]
  (let [args (into-array String ["content://call_log/calls" "--projection" "_id:number:duration:date:name"])]
    (get-results adb-shell-command adb-devices args)))

(defn download-devices-contacts
  [{:keys [adb-shell-command adb-devices]}]
  (let [args (into-array String ["content://com.android.contacts/data/phones" "--projection" "_id:display_name:contact_id:data1"])]
    (get-results adb-shell-command adb-devices args)))

(defn download-devices-sms
  [{:keys [adb-shell-command adb-devices]}]
  (let [args (into-array String ["content://sms" "--projection" "_id,address,body,date,creator,seen"])]
    (get-results adb-shell-command adb-devices args)))
