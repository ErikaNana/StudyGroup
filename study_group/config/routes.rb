StudyGroup::Application.routes.draw do
  root :to => "static#welcome"
  match 'create', to: 'static#create',     via: 'post'
  match 'create', to: 'static#display_json',     via: 'get'
end
