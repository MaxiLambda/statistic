(ns statistic-web.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [statistic-web.events :as events]
   [statistic-web.views.app :as views]
   ))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/app-panel] root-el)))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (mount-root))
