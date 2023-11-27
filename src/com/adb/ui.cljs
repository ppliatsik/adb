(ns ^:figwheel-hooks com.adb.ui
  (:require [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [secretary.core :as sec]
            [com.adb.ui.routes :as routes]
            [com.adb.ui.breadcrumb :as ui.breadcrumb]
            [com.adb.ui.navbar :as ui.navbar]
            [goog.object]
            [com.adb.ui.form :as form]
            [com.adb.ui.notifications-bar :as ui.notifications-bar]
            [com.adb.mobile.ui.view :as mobile.view]))

(rf/reg-fx
  :page-title
  (fn [title]
    (set! window.document.title title)))

(rf/reg-event-db
  ::set-view
  (fn [db [_ view]]
    (assoc db :ui/current-view view)))

(rf/reg-sub
  :ui/get-view
  (fn [db _]
    (:ui/current-view db)))

(defn main-view
  []
  [:article.box
   [ui.navbar/view]
   [:div.icons-wrapper.columns.is-multiline
    [:div.column
     [:a.button.is-link.m-1
      {:href (routes/mobile-data-view)}
      [form/icons {:icon     :mobile
                   :icon-css "fa-2x is-white"}]
      [:span "Προβολή και αναζήτηση αρχείων"]]]]])

(defn child-view []
  (fn []
    (let [current-view @(rf/subscribe [:ui/get-view])]
      (rf/dispatch [::ui.breadcrumb/set-breadcrumb current-view])
      (cond
        (= current-view :com.adb.mobile/view) [mobile.view/view]
        :else [main-view]))))

(defn app []
  (fn []
    [:div
     [:div.container
      [ui.notifications-bar/global-notifications-bar]
      [child-view]
      [:footer
       [:div.content.has-text-centered.mt-4
        [:div.content.has-text-centered.m-2.p-2
         [:span "Ψηφιακή Εγκληματολογία, Χειμερινό Εξάμηνο 2023-2024"]]]]]]))

(rf/reg-event-fx
  ::load-success
  (fn [_ _]
    {::visit-current-url []}))

(rf/reg-fx
  ::visit-current-url
  (fn [_]
    (sec/dispatch! (-> js/window .-location .-hash (subs 1)))))

(defn main-app
  "mount to root element"
  []
  (rf/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [(fn [] [app])] root-el)))

(defn init
  []
  (routes/setup-app-routes)
  (rf/dispatch-sync [::load-success])
  (main-app))

(defn ^:after-load re-render []
  (main-app))

(defonce start-up (do (init) true))
