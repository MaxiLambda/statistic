(ns statistic-web.views.login.re-frame.login-request-body)

;;{:type oneOf {"admin", "space"
;; :pw oneOf {string, nil}}
;;when :type "space" there are additional keys available:
;;  :id         -> int
;;  :name       -> string
;;  :permission -> oneOf {"edit", "view"}
;;when :type "admin" there are additional keys available:
;;  :name       -> string
(defn transform-body [{:keys [type pw id name permission]}]
  (merge {:password pw} (cond
                          (and (= type "space") (= permission "edit")) {:space    id
                                                                        :username (str name "-admin")}
                          (= type "space") {:space    id
                                            :username name}
                          (= type "admin") {:username name}
                          )))