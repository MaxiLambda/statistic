(ns statistic-web.views.error.re-frame.error-subs
  (:require [re-frame.core :as re-frame]))

;;subscription handler for the current view
(re-frame/reg-sub
  ::error
  (fn [db]
    (-> db :view :params :error)))