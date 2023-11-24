(ns com.adb.ui.form)

(def icon-shorthands
  {:question-mark 'fa-question-circle
   :cancel        'fa-times-circle
   :camera        'fa-camera
   :trash         'fa-trash
   :mobile        'fa-mobile
   :search        'fa-search})

(defn icons
  [{:keys [icon icon-css] :as options}]
  [:span.icon
   [:i.fas {:class (str icon-css " " (get icon-shorthands icon :question-mark))}]])
