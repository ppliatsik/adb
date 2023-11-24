(ns dev
  (:require [integrant.repl :as ig]
            [com.adb.system :as system]))

(defn reset []
      (integrant.repl/set-prep! (constantly (system/load-config "resources/config.edn")))
      (ig/reset))

(defn halt [] (ig/halt))

(defn sys [] integrant.repl.state/system)
(defn db [] (:db/pg (sys)))
(defn adb-devices [] (:adb/devices (sys)))
