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
        @user = schedule[:user]

        # [{name: name, courseInfo: [{day: days, time: times}, {day: days}, time: times]}]
        courses = schedule[:courses]

        #create course objects
        @courseArray = Array.new

        #create arrays
        courses.each do |course|
            days = ""
            times = ""
            day_and_times = course[:courseInfo] #array of hashmaps

            #build strings
            day_and_times.each do |info|
                days = days + info[:day] + ","
                times = times + info[:time] + ","
            end
            #remove last commas
            days.chop!
            times.chop!

            #create course objects
            #need to check if it exists first, but this can be done later
            add_course = Course.create(name: course[:name], days:days, times:times)
            #if it exists, just add that
            @courseArray.push(add_course)
        end
    end
end
