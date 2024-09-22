(ns statistic-web.views.archive.archive-view
  (:require [clojure.string :as str]
            [re-frame.core :as re-frame]
            [reagent-mui.material.grid :refer [grid]]
            [reagent-mui.material.typography :refer [typography]]
            [statistic-web.views.archive.re-frame.archive-subs :as subs]))

(defn centered-item
  "An Item in a grid container. Content is aligned in the center.
  The underlying typography component can be modified via the sx parameter."
  ([size text] (centered-item size text {}))
  ([size text sx]
   [grid {:xs             size
          :item           true
          :justifyContent "center"
          :alignItems     "center"}
    [typography
     {:sx (merge {:textAlign "center"} sx)}
     text
     ]
    ]))

(defn match-component [{:keys          [id datetime winner discipline tag]
                        {team1 :names} :team1
                        {team2 :names} :team2}]
  (let [time (js/Date. datetime)
        [team1-state team1-color] (case winner 0 ["" "blue"]
                                               1 ["Winner" "green"]
                                               2 ["Loser" "red"])
        [team2-state team2-color] (case winner 0 ["" "blue"]
                                               1 ["Loser" "red"]
                                               2 ["Winner" "green"])]
    [grid {:key id :container true}
     [grid {:item true}
      [:h3 (str "Match (" discipline "): " tag " - " time)]
      ]
     [centered-item 5 team1-state {:font-weight "bold"}]
     [centered-item 2 (when (= 0 winner) "Draw") {:font-weight "bold"}]
     [centered-item 5 team2-state {:font-weight "bold"}]
     [centered-item 5 (str/join ", " team1) {:border (str "1px solid " team1-color) :border-radius 2}]
     [centered-item 2 "vs." {:font-weight "bold"}]
     [centered-item 5 (str/join ", " team2) {:border (str "1px solid " team2-color) :border-radius 2}]
     ]))

(defn archive-view []
  (let [matches @(re-frame/subscribe [::subs/matches])]
    [:<>
     [:h1 "All matches"]
     (for [match matches]
       (match-component match))
     ]))

