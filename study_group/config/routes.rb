StudyGroup::Application.routes.draw do
  root :to => "static#welcome"
  match 'create', to: 'static#create',     via: 'get'
  match 'create', to: 'static#display_json',     via: 'post'
end
