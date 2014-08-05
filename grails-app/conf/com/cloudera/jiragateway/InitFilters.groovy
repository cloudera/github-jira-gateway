package com.cloudera.jiragateway

class InitFilters {

  def configService

  def filters = {
    all(controller: 'init', invert: true) {
      before = {
        if (!configService.appConfigured) {
          redirect(controller: 'init')
          return false
        }

        // If the last value is falsy and there is no explicit return statement then this filter method will
        // return a falsy value and cause requests to fail silently.
        return true
      }
    }
  }
}
