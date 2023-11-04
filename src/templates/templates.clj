(ns templates.templates
  (:require [hiccup.form :refer [form-to file-upload submit-button]]
            [garden.core :refer [css]]))

(def SITE-TITLE "Datbin")

(def styles
  (css [:* {:font-family "Monospace"}]
       [:div#about {:padding "1em 0em"}]))

(defn page-template [body & {:keys [title header]}]
  [:html {:lange "en"}
   [:head
    [:style styles]
    [:meta {:name "viewport" :content "width=device-width,minimum-scale=1"}]]
   (let [page-title (if title
                      (format "%s - %s" SITE-TITLE title)
                      SITE-TITLE)]
     [:title page-title])
   [:body
    [:h1 SITE-TITLE]
    [:h2 header]
    body]])

(def about
  [:div#about
   "Datbin is a pastebin for any data format. Select a file to upload and share with a generated link."])

(def upload-page
  (page-template
   [:div
    about
    (form-to
     {:enctype "multipart/form-data"}
     [:post "/receive"]
     (file-upload {:id "file"
                   :required true} "file")
     (submit-button "upload"))]
   :title "Upload"
   :header "Upload"))

(defn success-page [& {:keys [filename ref-id]}]
  (page-template
   [:div (-> "Your file \"%s\" has been saved into the database. Use this "
             (format filename))
    [:a {:href (str "/file/" ref-id)} "link"] " to access."]
   :title "Success"
   :header "Success"))

(def not-found-page
  (page-template
   [:div "This link is invalid."]
   :title "404"
   :header "404 Error"))