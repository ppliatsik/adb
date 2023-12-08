(ns com.adb.mobile.ui.sms.view
  (:require [com.adb.mobile.ui.sms.model :as model]
            [com.adb.ui.form :as form]
            [com.adb.ui.routes :as routes]
            [com.adb.ui.navbar :as ui.navbar]
            [re-frame.core :as rf]))

(defn- update-button
  []
  [:div.columns.box.mt-2.is-centered
   [:div.column.is-4.icons-wrapper
    [:a.button.is-warning.is-fullwidth
     {:href (routes/main-view)}
     [form/icons {:icon     :cancel
                  :icon-css "fa-2x"}]
     [:span "Πίσω"]]]
   [:div.column.is-4.icons-wrapper
    [:button.button.is-primary.is-fullwidth
     {:on-click (fn [_]
                  (rf/dispatch [::model/update-data]))}
     [form/icons {:icon     :search
                  :icon-css "fa-2x"}]
     [:span "Αναζήτηση"]]]])

(defn- show-data-line
  [{:keys [id device-id address body creator seen created-at]}]
  [:tr {:key (str device-id id)}
   [:td.nowrap device-id]
   [:td.nowrap address]
   [:td.nowrap body]
   [:td.nowrap creator]
   [:td.nowrap (if seen
                [form/icons {:icon     :check
                             :icon-css "fa-2x"}]
                "-")]
   [:td.nowrap (.format (js/moment created-at) "DD/MM/YYYY HH:mm")]])

(defn- show-data
  [{:keys [mobile-data]}]
  [:div.columns.has-backgroun-light
   [:table.table.is-fullwidth.is-stripped.is-hoverable.vertical-align-middle.fixed.has-background-light
    [:thead
     [:tr.has-text-centered
      [:th {:width "15%"} "Αναγνωριστικό συσκευής"]
      [:th {:width "15%"} "Προς"]
      [:th {:width "40%"} "Κείμενο"]
      [:th {:width "10%"} "Δημιουργός"]
      [:th {:width "10%"} "Το έχει δει"]
      [:th {:width "15%"} "Ημ/νία δημιουργίας"]]]
    [:tbody
     (map (fn [data]
            ^{:key (str (:device-id data) (:id data))}
            (show-data-line data))
          mobile-data)]]])

(defn view []
  (let [model @(rf/subscribe [::model/ui-model])]
    [:article.box
     [ui.navbar/view]
     [update-button]
     [show-data model]]))
