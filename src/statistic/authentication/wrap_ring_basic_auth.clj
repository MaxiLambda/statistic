(ns statistic.authentication.wrap-ring-basic-auth
  (:require [clojure.string :as s])
  (:import (java.util Base64)))

;;everything in this namespace was copied from ring.middleware.basic-authentication from the
;;[ring-basic-authentication/ring-basic-authentication "1.2.0"] package
;;the only change is that the authentication function now receives the whole request as a third parameter

(defn- byte-transform
  "Used to encode and decode strings.  Returns nil when an exception
  was raised."
  [direction-fn string]
  (try
    (s/join (map char (direction-fn (.getBytes ^String string))))
    (catch Exception _)))

(defn- decode-base64
  "Will do a base64 decoding of a string and return a string."
  [^String string]
  (byte-transform #(.decode (Base64/getDecoder) ^bytes %) string))

(defn authentication-failure
  "Returns an authentication failure response, which defaults to a
  plain text \"access denied\" response.  :status and :body can be
  overriden via keys in denied-response, and :headers from
  denied-response are merged into those of the default response.
  realm defaults to \"restricted area\" if not given.

  Note: this function not aware of what kind of request it is building
  a response for and thus can not avoid sending no body on a HEAD
  request.  The wrap-basic-authentication middleware will provide a
  customized denied-response but if used outside that wrapper consider
  setting :body to nil for HEAD requests."
  [& [realm denied-response]]
  (assoc (merge {:status 401
                 :body   "access denied"}
                denied-response)
    :headers (merge {"WWW-Authenticate" (format "Basic realm=\"%s\""
                                                (or realm "restricted area"))
                     "Content-Type"     "text/plain"}
                    (:headers denied-response))))

(defn basic-authentication-request
  "Authenticates the given request against using auth-fn. The value
  returned by auth-fn is assoc'd onto the request as
  :basic-authentication.  Thus, a truthy value of
  :basic-authentication on the returned request indicates successful
  authentication, and a false or nil value indicates authentication
  failure."
  [request auth-fn]
  (let [auth ((:headers request) "authorization")
        cred (and auth (decode-base64 (last (re-find #"^Basic (.*)$" auth))))
        [user pass] (and cred (s/split (str cred) #":" 2))]
    (assoc request :basic-authentication (and cred (auth-fn (str user) (str pass) request)))))

(defn wrap-basic-authentication
  "Wrap response with a basic authentication challenge as described in
  RFC2617 section 2.

  The authenticate function is called with three parameters, the userid, the
  password and the request object, and should return a value when the login is valid.
  This value is added to the request structure with the :basic-authentication
  key.

  The realm is a descriptive string visible to the visitor.  It,
  together with the canonical root URL, defines the protected resource
  on the server.

  The denied-response is a ring response structure which will be
  returned when authorization fails.  The appropriate status and
  authentication headers will be merged into it.  It defaults to plain
  text 'access denied' response."
  [app authenticate & [realm denied-response]]
  (fn [{:keys [request-method] :as req}]
    (let [auth-req (basic-authentication-request req authenticate)]
      (if (:basic-authentication auth-req)
        (app auth-req)
        (authentication-failure realm
                                (into denied-response
                                      (when (= request-method :head)
                                        {:body nil})))))))