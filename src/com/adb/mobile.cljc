(ns com.adb.mobile
  (:require [clojure.spec.alpha :as s]))

(s/def ::device-id string?)
(s/def ::file-name string?)
