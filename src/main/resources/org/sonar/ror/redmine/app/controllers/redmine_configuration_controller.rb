class RedmineConfigurationController < ApplicationController
  SECTION=Navigation::SECTION_RESOURCE
  
  before_filter :login_required, :load_resource, :load_properties
  
  # Checks if main parameters are given
  before_filter :only => [:test, :save] do |c|
    c.send(:check_mandotory_parameters)
    c.send(:check_parameters, :project_key)
    
    if !@error.empty?
      render_result
      return
    end
  end
  
  # Checks if all parameters for save are given
  before_filter :only => :save do |c|
    c.send(:check_parameters, :api_key, :priority_id, :tracker_id)
    
    if !@error.empty?
      render_result
      return
    end
  end
  
  verify :method => :post,
       :only => ['test', 'save'],
       :redirect_to => {:action => 'index'}
  
  @error = { }
  @result = { }
  
  # GET /page.redmine_configuration
  def index
    # Checks if all parameters are given to run connection text automatically
    if ((!@host.empty? or !@ghost.empty?) and (!@api_key.empty? or !@gapi_key.empty?) and !@project_key.empty? and @priority_id > 0 and @tracker_id > 0)
      @run_test = true
    else
      @run_test = false
    end    
  end
  
  # POST /page.redmine_configuration/test
  def test
    run_test
    
    render_result
  end
  
  def save
    Property.set(configuration::HOST, params[:host], @resource.id)
    Property.set(configuration::API_ACCESS_KEY, params[:api_key], @resource.id)
    Property.set(configuration::PROJECT_KEY, params[:project_key], @resource.id)
    
    Property.set(configuration::PRIORITY_ID, params[:priority_id], @resource.id)
    Property.set(configuration::TRACKER_ID, params[:tracker_id], @resource.id)
    
    render_result
  end
  
  private
  
  def run_test
    @result = { }
    user = nil
    trackers = nil
    
    host = nil
    if !params[:host].empty? 
      host = params[:host]
    elsif !@ghost.empty?
      host = @ghost
    end
    
    api_key = nil
    if !params[:api_key].empty?
      api_key = params[:api_key]
    elsif !@gapi_key.empty?
      api_key = @api_key
    end
    
    adp = java_facade.getComponentByClassname('redmine', 'org.sonar.plugins.redmine.client.RedmineAdapter')
    begin
      adp.connectToHost(host, api_key)
    rescue java_facade.getComponentByClassname('redmine', 'org.sonar.plugins.redmine.exceptions.RedmineGeneralException').class => e
      @error[:host] = e.getMessage
      return
    end
    
    begin
      user = adp.getCurrentUser
      @result[:user] = user.getFullName
      # Possible if the class is used in some way see classes in exceptions package
      #    rescue java_facade.getComponentByClassname('redmine', 'com.taskadapter.redmineapi.RedmineAuthenticationException').class => e
    rescue java_facade.getComponentByClassname('redmine', 'org.sonar.plugins.redmine.exceptions.RedmineAuthenticationException').class => e
      @error[:api_key] = message('page.redmine_configuration.test.api_key_is_wrong')
      return
    rescue java_facade.getComponentByClassname('redmine', 'org.sonar.plugins.redmine.exceptions.RedmineTransportException').class => e
      @error[:host] = message('page.redmine_configuration.test.host_or_port_wrong')
      return
    rescue java_facade.getComponentByClassname('redmine', 'org.sonar.plugins.redmine.exceptions.RedmineNotFoundException').class => e
      @error[:host] = message('page.redmine_configuration.test.url_error')
      return
    rescue java_facade.getComponentByClassname('redmine', 'org.sonar.plugins.redmine.exceptions.RedmineGeneralException').class => e
      @error[:general] = e
      return
    end
    
    begin
      project = adp.getProject(params[:project_key])
      @result[:project] = project.getName;
      
      trackers = project.getTrackers
      
      # Check if user is assigned to project (admin must be member, too!)
      if !adp.isMemberOfProject(user, project)
        @error[:project_key] = message('page.redmine_configuration.test.project_no_member')
        return
      end
    rescue java_facade.getComponentByClassname('redmine', 'org.sonar.plugins.redmine.exceptions.RedmineNotFoundException').class => e
      @error[:project_key] = message('page.redmine_configuration.test.project_key_invalid')
      return
    rescue java_facade.getComponentByClassname('redmine', 'org.sonar.plugins.redmine.exceptions.RedmineNotAuthorizedException').class => e
      @error[:project_key] = message('page.redmine_configuration.test.project_no_access')
      return
    rescue java_facade.getComponentByClassname('redmine', 'org.sonar.plugins.redmine.exceptions.RedmineGeneralException').class => e
      @error[:general] = e
      return
    end
    
    ts = { }
    trackers.each do |t|
      ts[t.getId] = t.getName
    end
    @result[:trackers] = ts
    
    ps = { }
    adp.getIssuePriorities.each do |p|
      ps[p.getId] = p.getName
    end
    @result[:priorities] = ps
  end
  
  def check_mandotory_parameters
    @error = { } unless !@error.nil?
    
    if params[:host].blank? and @ghost.blank?
      @error[:host] = message('page.redmine_configuration.error.host_is_missing')
    end
    
    if params[:api_key].blank? and !@gapi_key_available
      @error[:api_key] = message('page.redmine_configuration.error.api_key_is_missing')
    end
  end
  
  def check_parameters(*args)
    args.each do |arg_item|
      id = arg_item.to_s
      if params[id].blank?
        @error[id] = message('page.redmine_configuration.error.'+id+'_is_missing')
      end
    end
  end
  
  def load_properties
    @host = Property.value(configuration::HOST, @resource.id, "")
    @ghost = Property.value(configuration::HOST, nil, "")
    
    @api_key = Property.value(configuration::API_ACCESS_KEY, @resource.id, "")
    @gapi_key = Property.value(configuration::API_ACCESS_KEY, nil, "")
    @gapi_key_available = !@gapi_key.empty? ? true : false
    
    @project_key = Property.value(configuration::PROJECT_KEY, @resource.id, "")
    
    @priority_id = Property.value(configuration::PRIORITY_ID, @resource.id, 0).to_i
    @tracker_id = Property.value(configuration::TRACKER_ID, @resource.id, 0).to_i
  end
  
  def render_result
    @result = { } unless !@result.nil?
    
    if @error.empty?
      @result[:status] = 'success'
    else
      @result[:status] = 'error'
      @result[:error] = @error
    end
    
    render :json => @result.to_json
  end
  
  def load_resource
    require_parameters :id
    
    init_resource_for_user_role unless !params[:id]
    
    access_denied unless is_admin?(@resource)
  end
  
  def configuration
    java_facade.getComponentByClassname('redmine', 'org.sonar.plugins.redmine.config.RedmineSettings').class
  end
end