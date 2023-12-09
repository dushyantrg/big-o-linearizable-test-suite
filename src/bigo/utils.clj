(ns bigo.utils
  (:require [clojure.string :as str]))

(def dir  "/opt/bigo-raft")
(def binary "java /usr/bin/bigo-raft.jar")
(def logfile (str dir "/bigo-raft.log"))
(def pidfile (str dir "/bigo-raft.pid"))

(defn node-url
  "An HTTP url for connecting to a node on a particular port."
  [node port]
  (str "http://" node ":" port))

(defn peer-url
  "The HTTP url for other peers to talk to a node."
  [node]
  (node-url node 9001))

(defn client-url
  "The HTTP url clients use to talk to a node."
  [node]
  (node-url node 9000))

(defn initial-cluster
  "Constructs an initial cluster string for a test, like
  \"foo=foo:9000,bar=bar:9000,...\""
  [test]
  (->> (:nodes test)
       (map (fn [node]
              (str node "=" (peer-url node))))
       (str/join ",")))