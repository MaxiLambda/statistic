(ns statistic-web.views
  (:require
    [re-frame.core :as re-frame]
    [statistic-web.subs :as subs]
    [reagent-mui.material.button :refer [button]]))

;;create views - STEP 5

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        table (re-frame/subscribe [::subs/table])]
    [:div
     [:h1
      "Hello from " @name "!"]
     [button
      {:variant "contained"
      :color "primary"}
      "Click me"
      ]
     ]
    ))
