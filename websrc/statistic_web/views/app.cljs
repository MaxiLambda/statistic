(ns statistic-web.views.app
  (:require
    [re-frame.core :as re-frame]
    [statistic-web.re-frame.global-subs :as subs]
    [statistic-web.views.app-container :refer [app-container]]
    [statistic-web.views.app-header :refer [header]]
    [statistic-web.views.app-localization :refer [app-localization]]
    [statistic-web.views.archive.archive-view :refer [archive-view]]
    [statistic-web.views.error.error-view :refer [error-view]]
    [statistic-web.views.leaderboard.leaderboard-view :refer [leaderboard-view]]
    [statistic-web.views.management.management-view :refer [management-view]]))

;;create views - STEP 5

(defn views [view-key]
  (view-key {:leaderboard (leaderboard-view)
             :management  (management-view)
             :archive     (archive-view)
             :error       (error-view)}))

(defn app-panel []
  (-> @(re-frame/subscribe [::subs/view])
      views                                                 ;;resolve to the correct view
      app-container                                         ;;container for default body
      header
      app-localization))                                    ;;apply header to selected view
