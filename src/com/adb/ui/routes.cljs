(ns com.adb.ui.routes
  (:require [secretary.core :as sec]
            [re-frame.core :as rf]
            [goog.events :as events])
  (:import [goog History]
           [goog.history EventType]))

(defn history
  []
  (doto (History.)
    (events/listen EventType.NAVIGATE
                   (fn [event]
                     (let [fragment-url (.-token event)]
                       (sec/dispatch! fragment-url))))
    (.setEnabled true)))

(defn setup-app-routes
  []
  (sec/set-config! :prefix "#")

  (sec/defroute mobile-data-view "/mobile-data" []
    (rf/dispatch [:com.adb.ui/set-view :com.adb.mobile/view])
    (rf/dispatch [:com.adb.mobile.ui.model/init]))

  (sec/defroute main-view "/" []
    (rf/dispatch [:com.adb.ui/set-view :com.adb.ui/main-view]))

  (sec/defroute "*" []
    (rf/dispatch [:com.adb.ui/set-view :com.adb.ui/main-view]))

  (history))
