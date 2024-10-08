(ns statistic-web.views.login.login-view
  (:require [re-frame.core :as re-frame]
            [reagent-mui.material.button :refer [button]]
            [reagent-mui.material.form-control :refer [form-control]]
            [reagent-mui.material.grid :refer [grid]]
            [reagent-mui.material.input-label :refer [input-label]]
            [reagent-mui.material.menu-item :refer [menu-item]]
            [reagent-mui.material.select :refer [select]]
            [reagent-mui.material.text-field :refer [text-field]]
            [statistic-web.views.login.login-data :refer [login-data to-login]]
            [statistic-web.views.login.re-frame.login-events :as events]
            [statistic-web.views.login.re-frame.login-subs :as subs]))


(defn login-view []
  (let [spaces @(re-frame/subscribe [::subs/spaces])]
    [grid {:columns 1
           :style   {:border "1px solid black" :padding "1%"}
           :sx      {:ml "auto" :mr "auto" :max-width 300}}
     [:h3 "Login"]
     [form-control {:sx        {:mt 2}
                    :fullWidth true}
      [input-label {:id "login-label"
                    :sx {:bgcolor "background.default"}}
       "Login"
       ]
      [select {:labelId   "login-label"
               :variant   "outlined"
               :on-change #(->> % .-target .-value (to-login spaces) (reset! login-data))
               :value     (let [{:keys [id type]} @login-data]
                            (or (cond (and (= type "space") (-> id nil? not)) id
                                      (= type "admin") "admin")
                                ;;fallback if unset -> because mui complains on nil
                                ""))}
       (for [{:keys [id name]} spaces]
         [menu-item {:value id :key id} (str "Space: " name)])
       [menu-item {:value "admin" :key "admin"} "Admin"]
       ]
      ]
     (when (some-> @login-data :type (= "space"))
       [form-control {:sx        {:mt 2}
                      :fullWidth true}
        [input-label {:id "permission-label"
                      :sx {:bgcolor "background.default"}}
         "Permission"]
        [select {:labelId   "permission-label"
                 :variant   "outlined"
                 :on-change #(->> % .-target .-value (swap! login-data assoc :permission))
                 :value     (or (:permission @login-data) "")}
         [menu-item {:value "view" :id 1} "View"]
         [menu-item {:value "edit" :id 2} "Edit"]
         ]
        ])
     [form-control {:fullWidth true} (when (some-> @login-data :type (= "admin"))
       [text-field {:on-change #(->> % .-target .-value (swap! login-data assoc :name))
                    :value     (or (:name @login-data) "")
                    :placeholder "Admin-Username"
                    :fullWidth true
                    :sx {:mt 2}}])
     [text-field {:type      "password"
                  :sx {:mt 2}
                  :on-change #(->> % .-target .-value (swap! login-data assoc :pw))
                  :value     (or (:pw @login-data) "")
                  :placeholder "Password"}]
     [button {:variant  "contained"
              :sx {:mt 2}
              :disabled (nil? (:type @login-data))
              :on-click #(re-frame/dispatch [::events/login @login-data])}
      "Login"
      ]]
     ]))