(ns com.adb.ui.navbar
  (:require [re-frame.core :as rf]
            [com.adb.ui.breadcrumb :as ui.breadcrumb]))

(defn view []
  (let [breadcrumbs @(rf/subscribe [:ui/breadcrumbs])]
    [:div.has-background-warning-light.p-0.m-2
     [ui.breadcrumb/breadcrumb-list breadcrumbs]]))
