(ns server.server
  (:gen-class)
  (:require [io.pedestal.http :as server]
            [io.pedestal.http.route :as route]
            [server.service :as service]))

(defonce runnable-service (server/create-server service/service))

(defn run-dev []
  (-> service/service 
      (merge {:env :dev
              ::server/join? false
              ::server/routes #(route/expand-routes (deref #'service/routes))
              ::server/allowed-origins {:creds true :allowed-origins (constantly true)}
              ::server/secure-headers {:content-security-policy-settings {:object-src "'none'"}}})
      server/default-interceptors
      server/dev-interceptors
      server/create-server
      server/start))

(def server (atom (run-dev)))

(defn restart-server! []
  (server/stop @server)
  (reset! server (run-dev)))

(defn -main
  [& _]
  (println "\nCreating your server...")
  (server/start runnable-service))