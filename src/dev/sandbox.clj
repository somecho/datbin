(ns dev.sandbox
  (:require [server.server :refer [restart-server!]]
            [server.db :as db]
            [datalevin.core :as d]))

(restart-server!)
(d/entity db/db 1)