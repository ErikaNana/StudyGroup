StudyGroup::Application.routes.draw do
  root :to => "static#welcome"
  match 'create', to: 'static#create',     via: 'get'
  match 'create', to: 'static#display_group_formation',     via: 'post'
  match 'schedule', to: 'static#display_schedule', via: 'post'
  get '/accept_invitation/:group_name', to: 'static#accept_invitation', as: 'accept_invitation'
end
