(ns statistic-web.views.app
  (:require
    [re-frame.core :as re-frame]
    [statistic-web.subs :as subs]
    [statistic-web.views.app-header :refer [header]]
    [statistic-web.views.home :refer [home]]))

;;create views - STEP 5

;;TODO only a placeholder to check if events get dispatched as expected
(def something [:p "Some other text"])

(defn views [view-key]
  (view-key {:home  (home)
             :other something}))

(defn app-panel []
  (-> @(re-frame/subscribe [::subs/view])                   ;;get the view as a key
      views                                                 ;;resolve to the correct view
      header))                                              ;;apply header to selected view
