(ns com.adb.mobile.ui.contacts.model
  (:require [re-frame.core :as rf]
            [com.adb.ui.ajax]
            [com.adb.mobile :as mobile]))

(def paths [:ui/forms ::mobile/contacts :data])
(def data-path (rf/path [:ui/forms ::mobile/contacts :data]))
(def metadata
  {:data-path paths
   :fields    {}})

(rf/reg-event-fx
  ::init
  [data-path]
  (fn [{:keys [db]} _]
    {:db {}
     :fx [[:dispatch [::get-data]]]}))

(rf/reg-event-fx
  ::get-data
  [data-path]
  (fn [{:keys [db]} _]
    {:fx [[:dispatch [:ajax/get {:uri     "/api/mobile/contacts"
                                 :success ::get-data-success
                                 :failure ::get-data-failure}]]]}))

(rf/reg-event-db
  ::get-data-success
  [data-path]
  (fn [db [_ response]]
    (assoc db :mobile-data response)))

(rf/reg-event-fx
  ::get-data-failure
  [data-path]
  (fn [_ [_ response]]
    {:fx [[:dispatch [:ui/push-notification {:title "Error"
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-event-fx
  ::update-data
  [data-path]
  (fn [_ _]
    {:fx [[:dispatch [:ajax/post {:uri     "/api/mobile/contacts"
                                  :success ::update-data-success
                                  :failure ::update-data-failure}]]]}))

(rf/reg-event-fx
  ::update-data-success
  [data-path]
  (fn [_ _]
    {:fx [[:dispatch [::get-data]]]}))

(rf/reg-event-fx
  ::update-data-failure
  [data-path]
  (fn [_ [_ response]]
    {:fx [[:dispatch [:ui/push-notification {:title "Error"
                                             :body  (:reason response)
                                             :type  :error}]]]}))

(rf/reg-sub
  ::form-data
  (fn [db _]
    (get-in db paths)))

(rf/reg-sub
  ::ui-model
  :<- [::form-data]
  (fn [data _]
    (-> metadata
        (merge data))))
