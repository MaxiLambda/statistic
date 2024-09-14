(ns statistic-web.views.app
  (:require
    [re-frame.core :as re-frame]
    [statistic-web.subs.global-subs :as subs]
    [statistic-web.views.app-header :refer [header]]
    [statistic-web.views.leaderboard :refer [leaderboard]]
    [statistic-web.views.management :refer [management-view]]))

;;create views - STEP 5

;;TODO make prettier
(def error-view [:p "An error occurred"])

(defn views [view-key]
  (view-key {:leaderboard    (leaderboard)
             :management (management-view)
             :failure error-view}))

(defn app-panel []
  (-> @(re-frame/subscribe [::subs/view])
      :name                                                 ;;get the view as a key
      views                                                 ;;resolve to the correct view
      header))                                              ;;apply header to selected view
