(ns com.adb.mobile.ui.view
  (:require [com.adb.mobile.ui.model :as model]
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
  [{:keys [device-id file-name created-at]}]
  [:tr {:key (str device-id file-name)}
   [:td.nowrap device-id]
   [:td.nowrap file-name]
   [:td.nowrap (.format (js/moment created-at) "DD/MM/YYYY HH:mm")]
   [:td.nowrap [:a.button.is-info
                {:href   (str "/api/mobile/" device-id "/" file-name "/image.png")
                 :target "_blank"}
                [form/icons {:icon     :camera
                             :icon-css "fa"}]]]
   [:td.nowrap [:a.button.is-danger
                {:on-click (fn [_]
                             (rf/dispatch [::model/delete-data-record device-id file-name]))}
                [form/icons {:icon     :trash
                             :icon-css "fa"}]]]])

(defn- show-data
  [{:keys [mobile-data]}]
  [:div.columns.has-backgroun-light
   [:table.table.is-fullwidth.is-stripped.is-hoverable.vertical-align-middle.fixed.has-background-light
    [:thead
     [:tr.has-text-centered
      [:th {:width "20%"} "Αναγνωριστικό συσκευής"]
      [:th {:width "30%"} "Όνομα αρχείου"]
      [:th {:width "15%"} "Ημ/νία δημιουργίας"]
      [:th {:width "10%"} "Εικόνα"]
      [:th {:width "10%"} "Διαγραφή"]]]
    [:tbody
     (map (fn [data]
            ^{:key (str (:device-id data) (:file-name data))}
            (show-data-line data))
          mobile-data)]]])

(defn view []
  (let [model @(rf/subscribe [::model/ui-model])]
    [:article.box
     [ui.navbar/view]
     [update-button]
     [show-data model]]))
