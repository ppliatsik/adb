(ns com.adb.util
  (:require [clojure.string :as clj.str]))

(defn keyword->string
  "keyword -> string keeping qualified part"
  [k]
  (let [str-key (str k)]
    (if (clj.str/starts-with? str-key ":")
      (subs str-key 1)
      str-key)))
