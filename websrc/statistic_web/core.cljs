(ns statistic-web.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [statistic-web.views.app :as views]
   [statistic-web.re-frame.global-events :as events]
    ;;IMPORTANT: require all ns where events are defined
   [statistic-web.views.management.re-frame.management-events]
   [statistic-web.views.leaderboard.re-frame.leaderboard-events]))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/app-panel] root-el)))

;;web-app entrypoint
(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (mount-root))
