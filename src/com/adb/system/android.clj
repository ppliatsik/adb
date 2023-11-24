(ns com.adb.system.android
  (:require [integrant.core :as ig]
            [clojure.tools.logging :as log])
  (:import [java.io File]
           [se.vidstige.jadb JadbConnection JadbDevice RemoteFile]))

(defmethod ig/init-key :adb/devices [_ spec]
  (let [jadb    (JadbConnection.)
        devices (.getDevices jadb)]
    (log/info "Android devices found: " (count devices))
    devices))

(defmethod ig/halt-key! :adb/devices [_ adb-devices]
  )

(defn download-device-data
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
