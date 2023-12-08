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

  (sec/defroute mobile-photos "/photos" []
    (rf/dispatch [:com.adb.ui/set-view :com.adb.mobile/photos])
    (rf/dispatch [:com.adb.mobile.ui.photos.model/init]))

  (sec/defroute mobile-sms "/sms" []
    (rf/dispatch [:com.adb.ui/set-view :com.adb.mobile/sms])
    (rf/dispatch [:com.adb.mobile.ui.sms.model/init]))

  (sec/defroute mobile-calls "/calls" []
    (rf/dispatch [:com.adb.ui/set-view :com.adb.mobile/calls])
    (rf/dispatch [:com.adb.mobile.ui.calls.model/init]))

  (sec/defroute mobile-contacts "/contacts" []
    (rf/dispatch [:com.adb.ui/set-view :com.adb.mobile/contacts])
    (rf/dispatch [:com.adb.mobile.ui.contacts.model/init]))

  (sec/defroute main-view "/" []
    (rf/dispatch [:com.adb.ui/set-view :com.adb.ui/main-view]))

  (sec/defroute "*" []
    (rf/dispatch [:com.adb.ui/set-view :com.adb.ui/main-view]))

  (history))
