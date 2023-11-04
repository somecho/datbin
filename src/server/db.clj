(ns server.db
  (:require [datalevin.core :as d]))

(def DB-PATH "./data/datbindb")
(def schema {:name {:db/valueType :db.type/string}
             :ref-id {:db/valueType :db.type/string
                      :db/unique :db.unique/identity}})
(def conn (d/get-conn DB-PATH schema))
(def db (d/db conn))
(defn make-record [filename ref-id]
  (d/transact! conn [{:name filename :ref-id ref-id}]))
(defn get-filename [ref-id]
  (:name (d/entity db [:ref-id ref-id])))