(ns statistic-web.views.login.re-frame.login-subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::spaces
  (fn [db]
    (-> db :view :params :spaces)))