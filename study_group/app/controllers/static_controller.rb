class StaticController < ApplicationController

    def welcome
    end

    def create
    end

    def display_json
        #for now
        info = params[:createGroup]
        @info_size = info.size
        # returns hash map
        @user = info[:user]
        @members = params[:createGroup][:members]
    end

    def display_schedule
    end
end
