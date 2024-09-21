(ns statistic-web.views.archive.archive-view
  (:require [re-frame.core :as re-frame]
            [statistic-web.views.archive.re-frame.archive-subs :as subs]))

(defn archive-view []
  (let [matches @(re-frame/subscribe [::subs/matches])]
    [:<>
     [:h1 "Moin Moin"]
     [:p (str matches)]
     ]))