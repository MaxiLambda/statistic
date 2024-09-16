(ns statistic-web.views.management-view
  (:require [re-frame.core :as re-frame]
            [reagent-mui.material.form-control :refer [form-control]]
            [reagent-mui.material.text-field :refer [text-field]]
            [reagent-mui.x.date-time-picker :refer [date-time-picker]]
            [reagent-mui.material.select :refer [select]]
            [reagent-mui.material.menu-item :refer [menu-item]]
            [reagent-mui.material.input-label :refer [input-label]]
            [reagent.core :as r]
            [statistic-web.subs.management-subs :as subs]
            ["@mui/x-date-pickers/timeViewRenderers" :refer [renderTimeViewClock]]
            ["luxon" :refer [DateTime]]))

(def match-date (r/atom (.now DateTime)))

(defn management-view []
  (let [players @(re-frame/subscribe [::subs/players])
        tags @(re-frame/subscribe [::subs/tags])
        disciplines @(re-frame/subscribe [::subs/disciplines])]
    (println (str tags))
    (println (str disciplines))
    [form-control
     [:h3 "Create a new Match"]
     [date-time-picker {:label          "Date and Time of the Match"
                        :format         "dd/MM/yyyy - HH:mm"
                        :ampm           false
                        :value          @match-date
                        :on-change      #(reset! match-date %)
                        :view-renderers {:hours   renderTimeViewClock
                                         :minutes renderTimeViewClock}
                        }]
     [:br]
     [form-control
      [input-label {:id "winner-label"
                    :sx {:bgcolor "background.default"}}
       "Who won?"
       ]
      [select {:labelId "winner-label"
               :default-value 0
               :variant "outlined"}
       [menu-item {:value 0} "Draw"]
       [menu-item {:value 1} "Team 1"]
       [menu-item {:value 2} "Team 2"]
       ]]
     [:br]
     [text-field {:variant       "standard"
                  :label         "hallo"
                  :value (str tags)}]
     ]))


;;to create a new player
;;name -> char(20)



;;to create a new match
;;date
;;winner 0,1,2
;;discipline char(20)
;;tag char(20)
;;team1 -> [id]
;;team2 -> [id]