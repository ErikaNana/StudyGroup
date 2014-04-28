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
    schedule = params[:userSchedule]
    user = schedule[:user]
    # [{name: name, courseInfo: [{day: days, time: times}, {day: days}, time: times]}]
    courses = schedule[:courses]
    end
end
