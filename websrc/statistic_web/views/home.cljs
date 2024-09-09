(ns statistic-web.views.home
  (:require [re-frame.core :as re-frame]
            [reagent-mui.material.button :refer [button]]
            [reagent-mui.material.box :refer [box]]
            [statistic-web.subs :as subs]
            [statistic-web.events :as events]))

;;this needs to be a function for re-frame/subscribe
(defn home [] (let [name (re-frame/subscribe [::subs/name])]
                [box
                 {:sx {:pl 2
                       :pr 2}}
                 [:h1
                  "Hello from " @name "!"]
                 [button
                  {:variant  "contained"
                   :color    "primary"
                   :on-click #(re-frame/dispatch [::events/path-param-change :other])}
                  "Click me"
                  ]
                 ]))