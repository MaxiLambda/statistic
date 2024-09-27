(ns statistic-web.views.management.management-view
  (:require [re-frame.core :as re-frame]
            [reagent-mui.material.autocomplete :refer [autocomplete]]
            [reagent-mui.material.button :refer [button]]
            [reagent-mui.material.form-control :refer [form-control]]
            [reagent-mui.material.grid :refer [grid]]
            [reagent-mui.material.input-label :refer [input-label]]
            [reagent-mui.material.menu-item :refer [menu-item]]
            [reagent-mui.material.select :refer [select]]
            [reagent-mui.material.text-field :refer [text-field]]
            [reagent-mui.x.date-time-picker :refer [date-time-picker]]
            [reagent.core :as r]
            [statistic-web.views.management.management-data :refer [initial-match-form initial-new-player match-form match-form-valid new-player]]
            [statistic-web.views.management.re-frame.management-events :as events]
            [statistic-web.views.management.re-frame.management-subs :as subs]
            ["@mui/x-date-pickers/timeViewRenderers" :refer [renderTimeViewClock]]
            ["@mui/material" :as mui]))

(defn update-players
  "in-key is the key for which the selection was made
   out-key is the key for the opposing team
   selected-players are the ids of the selected players

   => this function sets in-key to selected-players and removes all elements in selected-players from out-key"
  [in-key out-key selected-players]
  (swap! match-form assoc in-key selected-players)
  (swap! match-form (fn [map] (assoc map out-key
                                         ;;calculate out-key without elements from in-key
                                         ;;=> use selected-players as set to check for membership
                                         (remove (set selected-players) (out-key map))))))

(defn management-view []
  (let [players @(re-frame/subscribe [::subs/players])
        tags @(re-frame/subscribe [::subs/tags])
        disciplines @(re-frame/subscribe [::subs/disciplines])]
    [grid {:columns 1 :style {:border "1px solid black" :padding "1%"}}
     [:h3 "Create a new Match"]
     [form-control {:fullWidth true :sx {:mt 2}}
      [date-time-picker {:label          "Date and Time of the Match"
                         :format         "dd/MM/yyyy - HH:mm"
                         :ampm           false
                         :value          (:date @match-form)
                         :on-change      #(swap! match-form assoc :date %)
                         :view-renderers {:hours   renderTimeViewClock
                                          :minutes renderTimeViewClock}
                         }]
      [form-control {:sx {:mt 2}}                                       ;;if select is not wrapped in its own form-control the label goes crazy
       [input-label {:id "winner-label"
                     :sx {:bgcolor "background.default"}}
        "Who won?"
        ]
       [select {:labelId   "winner-label"
                :value     (:winner @match-form)
                :on-change #(->> % .-target .-value (swap! match-form assoc :winner))
                :variant   "outlined"}
        [menu-item {:value 0 :key 0} "Draw"]
        [menu-item {:value 1 :key 1} "Team 1"]
        [menu-item {:value 2 :key 2} "Team 2"]
        ]]
      [grid {:columns 2 :display "flex" :sx {:mt 2}}
       [autocomplete {:fullWidth       true
                      :sx              {:pr 1}
                      :options         (or tags [])
                      :value           (or (:tag @match-form) "")
                      :freeSolo        true                 ;;enables custom values
                      :on-input-change #(->> %2 js->clj (swap! match-form assoc :tag)) ;;on-change only fires on custom values...
                      ;;https://stackoverflow.com/questions/63944323/problem-with-autocomplete-material-ui-react-reagent-clojurescript
                      :renderInput     #(do
                                          (set! (.-label %) "Match Tag")
                                          (r/create-element mui/TextField %))}
        ]
       [autocomplete {:fullWidth       true
                      :sx              {:pl 1}
                      :options         (or disciplines [])
                      :freeSolo        true                 ;;enables custom values
                      :value           (or (:discipline @match-form) "")
                      :on-input-change #(->> %2 js->clj (swap! match-form assoc :discipline)) ;;on-change only fires on custom values...
                      ;;https://stackoverflow.com/questions/63944323/problem-with-autocomplete-material-ui-react-reagent-clojurescript
                      :renderInput     #(do
                                          (set! (.-label %) "Match Discipline")
                                          (r/create-element mui/TextField %))}
        ]
       ]
      [grid {:columns 2 :display "flex" :sx {:mt 2}}
       [form-control {:fullWidth true
                      :sx        {:pr 1}}                   ;;if select is not wrapped in its own form-control the label goes crazy
        [input-label {:id "team1-label"
                      :sx {:bgcolor "background.default"}}
         "Team 1"
         ]
        [select {:labelId   "team1-label"
                 :multiple  true
                 :value     (:team1 @match-form)
                 :on-change #(->> % .-target .-value vec (update-players :team1 :team2))
                 :variant   "outlined"}
         (for [player players]
           [menu-item {:value (:id player) :key (:id player)} (:name player)])
         ]
        ]
       [form-control {:fullWidth true
                      :sx        {:pl 1}}                   ;;if select is not wrapped in its own form-control the label goes crazy
        [input-label {:id "team2-label"
                      :sx {:bgcolor "background.default"}}
         "Team 2"
         ]
        [select {:labelId   "team2-label"
                 :multiple  true
                 :value     (:team2 @match-form)
                 :on-change #(->> % .-target .-value vec (update-players :team2 :team1))
                 :variant   "outlined"}
         (for [player (remove (set (:team1 @match-form)) players)]
           [menu-item {:value (:id player) :key (:id player)} (:name player)])
         ]
        ]
       ]
      ;;disable button on invalid input
      [button {:variant  "contained"
               :disabled (not (match-form-valid @match-form))
               :on-click #(re-frame/dispatch [::events/create-match @match-form])
               :sx {:mt 2}}
       "Create match"]
      ]
     [:hr]
     [:h3 "Create a new Player"]
     (let [name (:name @new-player)
           existing (map :name players)
           invalid ((comp not nil? some) (set existing) [name])]
       [grid {:columns 2 :display "flex" :sx {:mt 2}}
        [form-control {:fullWidth true
                       :sx        {:mr 1}}
         [text-field {:placeholder "Player name"
                      :label       "Name"
                      :value       (:name @new-player)
                      :on-change   #(->> % .-target .-value (swap! new-player assoc :name))
                      :error       invalid}
          ]
         ]
        ;;disable button on invalid input
        [button {:variant   "contained"
                 :fullWidth true
                 :sx        {:ml 1}
                 :disabled  (or invalid (= 0 (count name)))
                 :on-click  #(re-frame/dispatch [::events/create-player @new-player])}
         "Create new Player"]
        ])
     ]
    ))

