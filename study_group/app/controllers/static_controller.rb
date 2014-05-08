class StaticController < ApplicationController

    def welcome
    end

    def create
    end

    def accept_invitation
        @group_name = params[:group_name]
    end

    def display_group_formation
        #for now
        info = params[:createGroup]
        course_name = info[:class]
        @course = Course.find_by_name(course_name)

        # returns hash map
        @master = Student.find_by_email(info[:user])
        members = info[:members]

        group_members = "";
        members.each do |member|
            name = member[:name]
            email = member[:email]
            group_members = group_members + email + ","
            #check if the member exists
            student = Student.find_by_email(email)
            if student.nil?
                #create a default student
                Student.create(name: name, email: email, student_type: "web")
            end
        end
        #get rid of the last comma
        group_members.chop!

        #need to do error checking on this later
        #construct the invited group
        @invited_group = InvitedGroup.create(name: @course.name + @master.name, members: group_members, master: @master.name)

        #initialize the confirmed group
        @confirmed_group = ConfirmedGroups.create(name: @course.name + @master.name, members: "", master: @master.name)

        #invite the members
        @invitees = Array.new
        members.each do |member|
            email = member[:email]
            invitee = Student.find_by_email(email)
            @invitees.push(invitee)
            StudentMailer.invitation_email(@master, invitee, @course, @invited_group).deliver
        end

    end

    #method to check if person is in a group
    #method to add person to a group

    #creates the student and their schedule
    def display_schedule
        schedule = params[:userSchedule]
        @master_name = schedule[:masterName]
        @user = schedule[:user]

        # [{name: name, courseInfo: [{day: days, time: times}, {day: days}, time: times]}]
        courses = schedule[:courses]
        schedule = ""


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

            #if doesn't exist, then create it
            add_course = nil
            course_name = course[:name]
            schedule = schedule + course_name + ","

            check_course = Course.find_by_name(course_name)
            if check_course
                @courseArray.push(check_course)
                @found = "found"
            else
                add_course = Course.create(name: course[:name], days:days, times:times)
                @found = "not found"
                #if it exists, just add that
                @courseArray.push(add_course)
            end
        end
        #create the student (one who created the group)
        schedule.chop!

        @student = Student.create(name: @master_name, schedule: schedule, email: @user, student_type: "android")
    end
end
