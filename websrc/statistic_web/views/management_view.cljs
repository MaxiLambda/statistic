(ns statistic-web.views.management-view
  (:require [re-frame.core :as re-frame]
            [reagent.core :as r]
            [reagent-mui.material.form-control :refer [form-control]]
            [reagent-mui.material.text-field :refer [text-field]]
            ["@mui/x-date-pickers/timeViewRenderers" :refer [renderTimeViewClock]]
            ["luxon" :refer [DateTime]]
            [reagent-mui.x.date-time-picker :refer [date-time-picker]]
            [statistic-web.subs.management-subs :as subs]))

(def match-date (r/atom (.now DateTime)))

(defn management-view []
  (let [players @(re-frame/subscribe [::subs/players])]
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
     []
     [text-field {:variant       "standard"
                  :label         "hello"
                  :default-value "hallo"}]]))


;;to create a new player
;;name -> char(20)



;;to create a new match
;;date
;;winner 0,1,2
;;discipline char(20)
;;tag char(20)
;;team1 -> [id]
;;team2 -> [id]