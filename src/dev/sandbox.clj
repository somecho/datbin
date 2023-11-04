(ns dev.sandbox
  (:require [server.server :refer [run-dev]]
            [io.pedestal.http :as server]
            [server.db :as db]
            [datalevin.core :as d]))

(def server (atom (run-dev)))

(defn restart-server! []
  (server/stop @server)
  (reset! server (run-dev)))

(restart-server!)
(d/entity db/db 1)