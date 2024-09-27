(ns statistic-web.core
  (:require
    [re-frame.core :as re-frame]
    [reagent.dom :as rdom]
    [statistic-web.re-frame.global-events :as events]
    [statistic-web.views.app :as views]
    ;;IMPORTANT: require all ns where events are defined
    [statistic-web.views.archive.re-frame.archive-events]
    [statistic-web.views.error.re-frame.error-events]
    [statistic-web.views.leaderboard.re-frame.leaderboard-events]
    [statistic-web.views.management.re-frame.management-events]))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/app-panel] root-el)))

;;web-app entrypoint
(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (mount-root))
