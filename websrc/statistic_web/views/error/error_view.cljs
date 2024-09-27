(ns statistic-web.views.error.error-view
  (:require [re-frame.core :as re-frame]
            [statistic-web.views.error.re-frame.error-subs :as subs]))

(defn error-view []
  (let [error @(re-frame/subscribe [::subs/error])]
    [:<>
     [:h2 "An error occurred"]
     [:p error]
     ]))
