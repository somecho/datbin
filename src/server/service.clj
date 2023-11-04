(ns server.service
  (:import [org.apache.commons.io IOUtils FileUtils])
  (:require [clojure.pprint :refer [pprint]]
            [clojure.java.io :as io]
            [hiccup2.core :as h]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.ring-middlewares :as ring-mw]
            [ring.util.response :as ring-resp]
            [server.db :as db]
            [templates.templates :as t]))

(def charset "abcdefghijklmnopqrstuvwxyzABCDEFGHIJLMNOPQRSTUVWXYZ0123456789")
(defn rand-identifier []
  (reduce str (repeatedly 12 #(rand-nth charset))))

(def log
  "Interceptor - pprints the request"
  {:name ::log
   :enter (fn [context]
            (pprint (:request context))
            context)})

(def write-file
  "Interceptor - writes request body as file to disk"
  {:name ::write-file
   :enter (fn [context]
            (let [[filename tmp] ((juxt :filename :tempfile)
                                  (get-in context [:request
                                                   :multipart-params
                                                   "file"]))
                  ref-id (rand-identifier)]
              (FileUtils/writeByteArrayToFile
               (io/file (str "data/" ref-id "/" filename))
               (IOUtils/toByteArray (io/input-stream tmp)))
              (assoc-in context [:request] {:upload-file-name filename
                                            :ref-id ref-id})))})

(def make-record
  "Interceptor - stores record of storage in database"
  {:name ::make-record
   :enter (fn [context]
            (let [[filename ref-id] ((juxt :upload-file-name :ref-id)
                                     (:request context))]
              (db/make-record filename ref-id)
              (assoc-in context [:request :ref-id] ref-id)))})

(defn home-page [_]
  (-> t/upload-page h/html str ring-resp/response))

(defn receive
  [{:keys [upload-file-name ref-id]}]
  (-> (t/success-page :filename upload-file-name :ref-id ref-id)
      (h/html)
      (str)
      (ring-resp/response)
      (ring-resp/content-type "text/html")))

(def common-interceptors [(body-params/body-params) http/html-body])

(def routes #{["/" :get (conj common-interceptors `home-page)]
              ["/receive" :post [(ring-mw/multipart-params)
                                 `log
                                 `write-file
                                 `make-record
                                 `receive]]})

(def service {:env :prod
              ::http/routes routes
              ::http/type :jetty
              ::http/host "0.0.0.0"
              ::http/port 3888})